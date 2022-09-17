<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
    <script src="ECharts/echarts.min.js"></script>
    <script src="jquery/jquery-1.11.1-min.js"></script>

    <script>
        $(function (){
            //在页面加载完毕后，绘制统计图表
            getCharts();


        })

        function getCharts(){
            $.ajax({
                url : "workbench/transaction/getCharts.do",
                type : "get",
                dataType : "json",
                success : function (data){

                    //基于准备好的dom，初始化echarts实例
                    let myChart = echarts.init(document.getElementById("main"))

                    // 指定图表的配置项和数据
                    let option = {
                        title: {
                            text: '交易漏斗图',
                            subtext: "统计交易阶段数量的漏斗图"
                        },
                        series: [
                            {
                                name: '交易漏斗图',
                                type: 'funnel',
                                left: '10%',
                                top: 60,
                                bottom: 60,
                                width: '80%',
                                min: 0,
                                max: data.total,
                                minSize: '0%',
                                maxSize: '100%',
                                sort: 'descending',
                                gap: 2,
                                label: {
                                    show: true,
                                    position: 'inside'
                                },
                                labelLine: {
                                    length: 10,
                                    lineStyle: {
                                        width: 1,
                                        type: 'solid'
                                    }
                                },
                                itemStyle: {
                                    borderColor: '#fff',
                                    borderWidth: 1
                                },
                                emphasis: {
                                    label: {
                                        fontSize: 20
                                    }
                                },
                                data: data.dataList
                                    /*[
                                        { value: 60, name: 'Visit' },
                                        { value: 40, name: 'Inquiry' },
                                        { value: 20, name: 'Order' },
                                        { value: 80, name: 'Click' },
                                        { value: 100, name: 'Show' }
                                    ]*/
                            }
                        ]
                    };

                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);

                }
            })

        }

    </script>
</head>
<body>

    <%-- 为ECharts准备一个具备大小（宽高）的DOM --%>
    <div id="main" style="width: 600px;height: 400px;"></div>

</body>
</html>
