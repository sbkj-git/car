function getSign(param) {  // 获取签名
    var appKey = "sbkjCarWebBEIJING";
    var securityKey = "2019sbkjCarWebBEIJINGsbkj";
    var timeStamp = formatDate(new Date(),"yyyyMMddHHmmss");
    if(param!=null&&param.length>0) {
        param = "appKey=" + appKey + "&timeStamp=" + timeStamp + "&" + param;
    }else {
        param = "appKey=" + appKey + "&timeStamp=" + timeStamp
    }
    return "?appKey="+appKey+"&timeStamp="+timeStamp+"&sign="+calculateSign(param,securityKey);
}

// 生成sign
function calculateSign(param,securityKey) {
    var params = param.split("&");
    param = params.sort().join("").replace(/=/g,"");
    console.info(param);
    return sha1(param+securityKey);
}


//格式化日期,
function formatDate(date, fmt) { //author: meizz
    var o = {
        "M+": date.getMonth() + 1,                 //月份
        "d+": date.getDate(),                    //日
        "H+": date.getHours(),                   //小时
        "m+": date.getMinutes(),                 //分
        "s+": date.getSeconds(),                 //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        "S": date.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}