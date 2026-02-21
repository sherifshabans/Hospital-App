// ============================================================
// screens/update_screen.dart
// شاشة عرض وتعديل Checklist محفوظة
// ============================================================

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/question.dart';
import '../providers/notes_provider.dart';
import 'checklist_screen.dart';

class UpdateScreen extends StatefulWidget {
  final int questionId;
  const UpdateScreen({super.key, required this.questionId});

  @override
  State<UpdateScreen> createState() => _UpdateScreenState();
}

class _UpdateScreenState extends State<UpdateScreen> {
  Question? _question;
  bool _loading = true;
  final Map<Item, bool> _expandedMap = {};

  @override
  void initState() {
    super.initState();
    _loadQuestion();
  }

  Future<void> _loadQuestion() async {
    final q = await context
        .read<NotesProvider>()
        .getQuestionById(widget.questionId);
    setState(() {
      _question = q;
      _loading = false;
    });
  }

  @override
  void dispose() {
    final provider = context.read<NotesProvider>();
    provider.resetValues();
    provider.resetSelectedOptionsMap();
    super.dispose();
  }

  Future<void> _saveAndPop() async {
    if (_question != null) {
      await context.read<NotesProvider>().updateQuestion(_question!);
    }
    if (mounted) Navigator.pop(context);
  }

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<NotesProvider>();

    return Scaffold(
      appBar: AppBar(
        title: Text(_question?.checkList ?? 'تعديل'),
        backgroundColor: Theme.of(context).colorScheme.primary,
        foregroundColor: Theme.of(context).colorScheme.onPrimary,
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _saveAndPop,
        child: const Icon(Icons.check_rounded),
      ),
      body: _loading
          ? const Center(child: CircularProgressIndicator())
          : _question == null
              ? const Center(child: Text('لم يتم العثور على القائمة'))
              : ListView.builder(
                  padding: const EdgeInsets.all(8),
                  itemCount: _question!.items.length,
                  itemBuilder: (context, idx) {
                    final item = _question!.items[idx];
                    final isExpanded = _expandedMap[item] ?? false;
                    final selectedOptions =
                        provider.getSelectedOptions2(item);

                    return Card(
                      margin: const EdgeInsets.symmetric(vertical: 6),
                      child: Column(
                        children: [
                          // ---- Header ----
                          ListTile(
                            title: Text(
                              item.title,
                              style: const TextStyle(
                                  fontWeight: FontWeight.bold),
                            ),
                            trailing: IconButton(
                              icon: Icon(
                                isExpanded
                                    ? Icons.keyboard_arrow_up
                                    : Icons.keyboard_arrow_down,
                              ),
                              onPressed: () => setState(
                                  () => _expandedMap[item] = !isExpanded),
                            ),
                          ),
                          // ---- Sub items ----
                          if (isExpanded)
                            Padding(
                              padding: const EdgeInsets.symmetric(
                                  horizontal: 12, vertical: 4),
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: item.subItems
                                    .asMap()
                                    .entries
                                    .map((entry) {
                                  final subIdx = entry.key;
                                  final answer = entry.value;
                                  return Padding(
                                    padding: const EdgeInsets.symmetric(
                                        vertical: 4),
                                    child: Column(
                                      crossAxisAlignment:
                                          CrossAxisAlignment.start,
                                      children: [
                                        Text(
                                          '${subIdx + 1}: ${answer.question}',
                                          maxLines: 3,
                                          overflow: TextOverflow.ellipsis,
                                        ),
                                        const SizedBox(height: 6),
                                        RadioButtonGroup(
                                          selectedOption:
                                              selectedOptions[subIdx] == -1
                                                  ? answer.answer
                                                  : selectedOptions[subIdx],
                                          onSelected: (option) {
                                            provider.updateSelectedOption2(
                                                item, subIdx, option);
                                            provider.updateAnswer2(
                                              item,
                                              Answer(
                                                  question: answer.question,
                                                  answer: option),
                                            );
                                            // تحديث السؤال
                                            final updatedAnswers =
                                                provider.getAnswers2(item);
                                            setState(() {
                                              _question!.items[idx] = Item(
                                                title: item.title,
                                                subItems:
                                                    List.from(updatedAnswers),
                                              );
                                            });
                                          },
                                        ),
                                        const Divider(),
                                      ],
                                    ),
                                  );
                                }).toList(),
                              ),
                            ),
                          // ---- Total sum ----
                          Padding(
                            padding: const EdgeInsets.all(8),
                            child: Text(
                              'المجموع: ${selectedOptions.where((o) => o != -1).fold(0, (a, b) => a + b)}',
                              style: const TextStyle(
                                  fontWeight: FontWeight.bold),
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
