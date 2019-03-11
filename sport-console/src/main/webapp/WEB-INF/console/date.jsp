<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div class="date">
 <span>现在时间： 
 	<script type="text/javascript">
        var weekday = "";
        var myDate=new Date();
        var myWeekday=myDate.getDay();
        var myMonth=myDate.getMonth()+1;
        var myDay= myDate.getDate();
        var year= myDate.getFullYear();
        if(myWeekday == 0)
            weekday=" 星期日 ";
        else if(myWeekday == 1)
            weekday=" 星期一 ";
        else if(myWeekday == 2)
            weekday=" 星期二 ";
        else if(myWeekday == 3)
            weekday=" 星期三 ";
        else if(myWeekday == 4)
            weekday=" 星期四 ";
        else if(myWeekday == 5)
            weekday=" 星期五 ";
        else if(myWeekday == 6)
            weekday=" 星期六 ";
        document.write(year+"年"+myMonth+"月"+myDay+"日 "+weekday);
      </script>
 </span>
</div> 