// ============================================================
// models/question.dart
// نموذج بيانات الـ Question المحفوظة في الـ Database
// ============================================================

import 'dart:convert';

class Answer {
  final String question;
  final int answer;

  Answer({required this.question, required this.answer});

  factory Answer.fromJson(Map<String, dynamic> json) {
    return Answer(
      question: json['question'] as String,
      answer: json['answer'] as int,
    );
  }

  Map<String, dynamic> toJson() => {
        'question': question,
        'answer': answer,
      };

  Answer copyWith({String? question, int? answer}) {
    return Answer(
      question: question ?? this.question,
      answer: answer ?? this.answer,
    );
  }
}

class Item {
  final String title;
  List<Answer> subItems;

  Item({required this.title, required this.subItems});

  factory Item.fromJson(Map<String, dynamic> json) {
    return Item(
      title: json['title'] as String,
      subItems: (json['subItems'] as List)
          .map((e) => Answer.fromJson(e as Map<String, dynamic>))
          .toList(),
    );
  }

  Map<String, dynamic> toJson() => {
        'title': title,
        'subItems': subItems.map((e) => e.toJson()).toList(),
      };
}

class Question {
  final int? id;
  String checkList;
  List<Item> items;
  final int dateAdded;

  Question({
    this.id,
    required this.checkList,
    required this.items,
    required this.dateAdded,
  });

  factory Question.fromMap(Map<String, dynamic> map) {
    return Question(
      id: map['id'] as int?,
      checkList: map['checkList'] as String,
      items: (jsonDecode(map['items'] as String) as List)
          .map((e) => Item.fromJson(e as Map<String, dynamic>))
          .toList(),
      dateAdded: map['dateAdded'] as int,
    );
  }

  Map<String, dynamic> toMap() => {
        if (id != null) 'id': id,
        'checkList': checkList,
        'items': jsonEncode(items.map((e) => e.toJson()).toList()),
        'dateAdded': dateAdded,
      };

  Question copyWith({
    int? id,
    String? checkList,
    List<Item>? items,
    int? dateAdded,
  }) {
    return Question(
      id: id ?? this.id,
      checkList: checkList ?? this.checkList,
      items: items ?? this.items,
      dateAdded: dateAdded ?? this.dateAdded,
    );
  }
}
