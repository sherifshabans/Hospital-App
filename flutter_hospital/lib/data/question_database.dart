// ============================================================
// data/question_database.dart
// قاعدة البيانات المحلية باستخدام sqflite (بديل Room)
// ============================================================

import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';
import '../models/question.dart';

class QuestionDatabase {
  static final QuestionDatabase instance = QuestionDatabase._init();
  static Database? _database;

  QuestionDatabase._init();

  Future<Database> get database async {
    if (_database != null) return _database!;
    _database = await _initDB('question.db');
    return _database!;
  }

  Future<Database> _initDB(String filePath) async {
    final dbPath = await getDatabasesPath();
    final path = join(dbPath, filePath);

    return await openDatabase(
      path,
      version: 1,
      onCreate: _createDB,
    );
  }

  Future<void> _createDB(Database db, int version) async {
    await db.execute('''
      CREATE TABLE questions (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        checkList TEXT NOT NULL,
        items TEXT NOT NULL,
        dateAdded INTEGER NOT NULL
      )
    ''');
  }

  // حفظ أو تعديل سؤال
  Future<int> upsertQuestion(Question question) async {
    final db = await database;
    if (question.id == null) {
      return await db.insert('questions', question.toMap());
    } else {
      await db.update(
        'questions',
        question.toMap(),
        where: 'id = ?',
        whereArgs: [question.id],
      );
      return question.id!;
    }
  }

  // جلب كل الأسئلة مرتبة بالتاريخ
  Future<List<Question>> getAllQuestions() async {
    final db = await database;
    final maps = await db.query(
      'questions',
      orderBy: 'dateAdded DESC',
    );
    return maps.map((e) => Question.fromMap(e)).toList();
  }

  // جلب سؤال بالـ id
  Future<Question?> getQuestionById(int id) async {
    final db = await database;
    final maps = await db.query(
      'questions',
      where: 'id = ?',
      whereArgs: [id],
    );
    if (maps.isEmpty) return null;
    return Question.fromMap(maps.first);
  }

  // حذف سؤال
  Future<void> deleteQuestion(int id) async {
    final db = await database;
    await db.delete('questions', where: 'id = ?', whereArgs: [id]);
  }

  Future<void> close() async {
    final db = await database;
    await db.close();
  }
}
