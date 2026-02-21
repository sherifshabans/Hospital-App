// ============================================================
// models/check_list_models.dart
// نماذج بيانات الـ Checklist المحمّلة من الـ JSON
// ============================================================

class CheckList {
  final String checkList;
  final List<CheckListItem> items;

  CheckList({required this.checkList, required this.items});

  factory CheckList.fromJson(Map<String, dynamic> json) {
    return CheckList(
      checkList: json['checkList'] as String,
      items: (json['items'] as List)
          .map((e) => CheckListItem.fromJson(e as Map<String, dynamic>))
          .toList(),
    );
  }
}

class CheckListItem {
  final String title;
  final List<String> subItems;

  CheckListItem({required this.title, required this.subItems});

  factory CheckListItem.fromJson(Map<String, dynamic> json) {
    return CheckListItem(
      title: json['title'] as String,
      subItems: List<String>.from(json['subItems'] as List),
    );
  }
}
