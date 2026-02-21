// ============================================================
// screens/checklist_screen.dart
// شاشة ملء الـ Checklist بـ RadioButtons (0, 1, 2)
// ============================================================

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../data/check_data.dart';
import '../models/check_list_models.dart';
import '../models/question.dart';
import '../providers/notes_provider.dart';

class CheckListScreen extends StatefulWidget {
  final String argument;
  const CheckListScreen({super.key, required this.argument});

  @override
  State<CheckListScreen> createState() => _CheckListScreenState();
}

class _CheckListScreenState extends State<CheckListScreen> {
  final Map<CheckListItem, bool> _expandedMap = {};
  // currentItems تحفظ الـ Items اللي هتُحفظ في الـ DB
  final List<Item> _subitems = [];

  @override
  void dispose() {
    final provider = context.read<NotesProvider>();
    provider.resetValues();
    provider.resetSelectedOptionsMap();
    super.dispose();
  }

  Future<void> _saveAndPop() async {
    final provider = context.read<NotesProvider>();
    final question = Question(
      checkList: widget.argument,
      items: List.from(_subitems),
      dateAdded: DateTime.now().millisecondsSinceEpoch,
    );
    await provider.saveNote(question);
    if (mounted) Navigator.pop(context);
  }

  @override
  Widget build(BuildContext context) {
    final checkList = CheckData.instance.getCheckListByName(widget.argument);
    final provider = context.watch<NotesProvider>();

    return Scaffold(
      appBar: AppBar(
        title: Text(widget.argument),
        backgroundColor: Theme.of(context).colorScheme.primary,
        foregroundColor: Theme.of(context).colorScheme.onPrimary,
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _saveAndPop,
        child: const Icon(Icons.check_rounded),
      ),
      body: checkList == null
          ? const Center(child: Text('القائمة غير موجودة'))
          : ListView.builder(
              padding: const EdgeInsets.all(8),
              itemCount: checkList.items.length,
              itemBuilder: (context, idx) {
                final item = checkList.items[idx];
                final isExpanded = _expandedMap[item] ?? false;
                final selectedOptions = provider.getSelectedOptions(item);

                return Card(
                  margin: const EdgeInsets.symmetric(vertical: 6),
                  child: Column(
                    children: [
                      // ---- Header ----
                      ListTile(
                        title: Text(
                          item.title,
                          style: const TextStyle(fontWeight: FontWeight.bold),
                        ),
                        trailing: IconButton(
                          icon: Icon(
                            isExpanded
                                ? Icons.keyboard_arrow_up
                                : Icons.keyboard_arrow_down,
                          ),
                          onPressed: () {
                            setState(() {
                              _expandedMap[item] = !isExpanded;
                            });
                          },
                        ),
                      ),
                      // ---- Sub items ----
                      if (isExpanded)
                        Padding(
                          padding: const EdgeInsets.symmetric(
                              horizontal: 12, vertical: 4),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              ...item.subItems.asMap().entries.map((entry) {
                                final subIdx = entry.key;
                                final subItem = entry.value;
                                return Padding(
                                  padding:
                                      const EdgeInsets.symmetric(vertical: 4),
                                  child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                        '${subIdx + 1}: $subItem',
                                        maxLines: 3,
                                        overflow: TextOverflow.ellipsis,
                                      ),
                                      const SizedBox(height: 6),
                                      RadioButtonGroup(
                                        selectedOption:
                                            selectedOptions[subIdx],
                                        onSelected: (option) {
                                          provider.updateSelectedOption(
                                              item, subIdx, option);
                                          provider.updateAnswer(
                                              item,
                                              Answer(
                                                  question: subItem,
                                                  answer: option));
                                          // تحديث الـ subitems
                                          final answers =
                                              provider.getAnswers(item);
                                          final existing = _subitems
                                              .indexWhere((i) =>
                                                  i.title == item.title);
                                          final newItem = Item(
                                              title: item.title,
                                              subItems: List.from(answers));
                                          if (existing != -1) {
                                            _subitems[existing] = newItem;
                                          } else {
                                            _subitems.add(newItem);
                                          }
                                        },
                                      ),
                                      const Divider(),
                                    ],
                                  ),
                                );
                              }),
                            ],
                          ),
                        ),
                      // ---- Total sum ----
                      Padding(
                        padding: const EdgeInsets.all(8),
                        child: Text(
                          'المجموع: ${selectedOptions.where((o) => o != -1).fold(0, (a, b) => a + b)}'
                          '   صفر: ${provider.zero}   واحد: ${provider.one}   اثنان: ${provider.two}',
                          style:
                              const TextStyle(fontWeight: FontWeight.bold),
                        ),
                      ),
                    ],
                  ),
                );
              },
            ),
    );
  }
}

// ---- RadioButtonGroup Widget ----
class RadioButtonGroup extends StatelessWidget {
  final int selectedOption;
  final ValueChanged<int> onSelected;

  const RadioButtonGroup({
    super.key,
    required this.selectedOption,
    required this.onSelected,
  });

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [0, 1, 2].map((option) {
        return Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Radio<int>(
              value: option,
              groupValue: selectedOption,
              onChanged: (val) => onSelected(val!),
              activeColor: Theme.of(context).colorScheme.primary,
            ),
            Text(option.toString()),
            const SizedBox(width: 8),
          ],
        );
      }).toList(),
    );
  }
}
