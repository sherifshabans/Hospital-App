// ============================================================
// data/check_data.dart
// تحميل بيانات الـ Checklist من ملف JSON في الـ assets
// ============================================================

import 'dart:convert';
import 'package:flutter/services.dart';
import '../models/check_list_models.dart';

class CheckData {
  static final CheckData instance = CheckData._();
  CheckData._();

  List<CheckList> _checkListItems = [];

  List<CheckList> get checkListItems => _checkListItems;

  Future<List<CheckList>> initList() async {
    if (_checkListItems.isEmpty) {
      final jsonString = await rootBundle.loadString('assets/checkList.json');
      final jsonList = jsonDecode(jsonString) as List;
      _checkListItems =
          jsonList.map((e) => CheckList.fromJson(e as Map<String, dynamic>)).toList();
    }
    return _checkListItems;
  }

  CheckList? getCheckListByName(String name) {
    try {
      return _checkListItems.firstWhere((c) => c.checkList == name);
    } catch (_) {
      return null;
    }
  }
}
