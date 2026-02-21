// ============================================================
// screens/start_screen.dart
// شاشة اختيار نوع الـ Checklist
// ============================================================

import 'package:flutter/material.dart';
import '../data/check_data.dart';

class StartScreen extends StatefulWidget {
  const StartScreen({super.key});

  @override
  State<StartScreen> createState() => _StartScreenState();
}

class _StartScreenState extends State<StartScreen> {
  String? _selectedItem;

  @override
  Widget build(BuildContext context) {
    final checkListNames = CheckData.instance.checkListItems
        .map((c) => c.checkList)
        .toList();

    return Scaffold(
      appBar: AppBar(
        title: const Text('اختر القائمة'),
        backgroundColor: Theme.of(context).colorScheme.primary,
        foregroundColor: Theme.of(context).colorScheme.onPrimary,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            const SizedBox(height: 20),
            const Text(
              'اختر نوع القائمة:',
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.w600),
            ),
            const SizedBox(height: 12),
            DropdownButtonFormField<String>(
              value: _selectedItem,
              decoration: InputDecoration(
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
                contentPadding:
                    const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
              ),
              hint: const Text('اختر...'),
              items: checkListNames
                  .map((name) => DropdownMenuItem(
                        value: name,
                        child: Text(name),
                      ))
                  .toList(),
              onChanged: (value) => setState(() => _selectedItem = value),
            ),
            const SizedBox(height: 30),
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                padding: const EdgeInsets.symmetric(vertical: 14),
                shape: RoundedCornerShape(12),
              ),
              onPressed: _selectedItem == null
                  ? null
                  : () {
                      Navigator.pushNamed(
                        context,
                        '/checklist',
                        arguments: _selectedItem,
                      );
                    },
              child: Text(
                _selectedItem != null
                    ? 'متابعة: $_selectedItem'
                    : 'اختر قائمة أولاً',
                style: const TextStyle(fontSize: 16),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

// Helper لعمل RoundedCornerShape
class RoundedCornerShape extends RoundedRectangleBorder {
  const RoundedCornerShape(double radius)
      : super(
          borderRadius: const BorderRadius.all(Radius.circular(12)),
        );
}
