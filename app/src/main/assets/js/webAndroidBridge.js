function showAndroidToast(jsonArrayData) {
    // 假设 jsonData 是一个JavaScript对象，我们将其转换为JSON字符串
    var jsonString = JSON.stringify(jsonArrayData);
    JsExtApollo.execAndroidFunc("showToastByJs", jsonString);
}

function execAndroidFunc(name, jsonArrayData) {
    var jsonString = JSON.stringify(jsonArrayData);
    JsExtApollo.execAndroidFunc(name,jsonString);
}