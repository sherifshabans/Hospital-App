// ============================================================
// providers/notes_provider.dart
// مكافئ NotesViewModel في Kotlin - إدارة الحالة بـ Provider
// ============================================================

import 'package:flutter/foundation.dart';
import '../data/question_database.dart';
import '../models/check_list_models.dart';
import '../models/question.dart';

class NotesProvider extends ChangeNotifier {
  final QuestionDatabase _db = QuestionDatabase.instance;

  List<Question> _notes = [];
  List<Question> get notes => _notes;

  // -------------------------------------------------------
  // counters (zero/one/two) لحساب مجموع الإجابات
  // -------------------------------------------------------
  int _zero = 0;
  int _one = 0;
  int _two = 0;
  int get zero => _zero;
  int get one => _one;
  int get two => _two;

  void increaseZero() { _zero++; notifyListeners(); }
  void increaseOne()  { _one++;  notifyListeners(); }
  void increaseTwo()  { _two++;  notifyListeners(); }

  void resetValues() {
    _zero = 0; _one = 0; _two = 0;
    notifyListeners();
  }

  // -------------------------------------------------------
  // selectedOptionsMap - لتتبع الـ RadioButton المختار
  // -------------------------------------------------------
  final Map<CheckListItem, List<int>> _selectedOptionsMap = {};
  Map<CheckListItem, List<int>> get selectedOptionsMap =>
      Map.unmodifiable(_selectedOptionsMap);

  List<int> getSelectedOptions(CheckListItem item) {
    return _selectedOptionsMap.putIfAbsent(
      item,
      () => List.filled(item.subItems.length, -1),
    );
  }

  void updateSelectedOption(CheckListItem item, int index, int option) {
    final list = _selectedOptionsMap.putIfAbsent(
      item,
      () => List.filled(item.subItems.length, -1),
    );
    list[index] = option;
    notifyListeners();
  }

  void resetSelectedOptionsMap() {
    _selectedOptionsMap.clear();
    notifyListeners();
  }

  // -------------------------------------------------------
  // answersMap - لتخزين الإجابات لكل item
  // -------------------------------------------------------
  final Map<CheckListItem, List<Answer>> _answersMap = {};

  List<Answer> getAnswers(CheckListItem item) {
    return _answersMap.putIfAbsent(item, () => []);
  }

  void updateAnswer(CheckListItem item, Answer answer) {
    final answers = _answersMap.putIfAbsent(item, () => []);
    final idx = answers.indexWhere((a) => a.question == answer.question);
    if (idx != -1) {
      answers[idx] = answer;
    } else {
      answers.add(answer);
    }
    notifyListeners();
  }

  // -------------------------------------------------------
  // selectedOptionsMap2 و answersMap2 للـ UpdateScreen
  // -------------------------------------------------------
  final Map<Item, List<int>> _selectedOptionsMap2 = {};
  Map<Item, List<int>> get selectedOptionsMap2 =>
      Map.unmodifiable(_selectedOptionsMap2);

  List<int> getSelectedOptions2(Item item) {
    return _selectedOptionsMap2.putIfAbsent(
      item,
      () => List.filled(item.subItems.length, -1),
    );
  }

  void updateSelectedOption2(Item item, int index, int option) {
    final list = _selectedOptionsMap2.putIfAbsent(
      item,
      () => List.filled(item.subItems.length, -1),
    );
    list[index] = option;
    notifyListeners();
  }

  final Map<Item, List<Answer>> _answersMap2 = {};

  List<Answer> getAnswers2(Item item) {
    return _answersMap2.putIfAbsent(item, () => []);
  }

  void updateAnswer2(Item item, Answer answer) {
    final answers = _answersMap2.putIfAbsent(item, () => []);
    final idx = answers.indexWhere((a) => a.question == answer.question);
    if (idx != -1) {
      answers[idx] = answer;
    } else {
      answers.add(answer);
    }
    notifyListeners();
  }

  // -------------------------------------------------------
  // Database operations
  // -------------------------------------------------------
  Future<void> loadNotes() async {
    _notes = await _db.getAllQuestions();
    notifyListeners();
  }

  Future<void> saveNote(Question question) async {
    await _db.upsertQuestion(question);
    await loadNotes();
  }

  Future<void> updateQuestion(Question question) async {
    await _db.upsertQuestion(question);
    await loadNotes();
  }

  Future<Question?> getQuestionById(int id) async {
    return await _db.getQuestionById(id);
  }

  Future<void> deleteQuestion(int id) async {
    await _db.deleteQuestion(id);
    await loadNotes();
  }
}
