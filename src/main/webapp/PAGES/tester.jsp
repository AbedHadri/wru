
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Test Service</title>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
        <script>
            $(document).on("submit", "#form-test", function (event) {

                event.preventDefault();
                var method = $("#method").val();
                var json = {};
                for (var i = 0; i < max; ++i) {
                    json[$("#i" + i + "-n").val()] = $("#i" + i).val();
                }
                json = JSON.stringify(json);
                $.ajax({
                    url: "/" + $("#url").val(),
                    type: method,
                    dataType: "json",
                    contentType: "application/json;charset=utf-8",
                    data: json,
                    success: function (response) {
                        $("#ans").text(JSON.stringify(response));
                    },
                    error: function (xhr, status, errorThrown) {
                        $("#ans").html(JSON.stringify(xhr.responseText).split("\n").join("<br>"));
                    }
                });
            });

            var max = 0;
            $(document).on("submit", "#tr1", function (event) {
                event.preventDefault();
                $("#app").html("");
                max = $("#kriter").val();
                for (var i = 0; i < max; ++i) {
                    $("#app").append(" name: <input type='text' id='i" + i + "-n'/> value: <input type='text' id='i" + i + "'/><br/>");
                }
            });
        </script>


    </head>
    <body>

        <div style='margin: 15px;'>
            <h1>Services are tested here</h1>
            <form id="tr1">Parameters Count: <input type='text' id='kriter'/><input type="submit" value="Generate Fields"/></form>
            <br/>
            <form id='form-test' >

                url: <input type='text' id='url' style="width:400px" placeholder="hostname/"/><br/><br/>
                method: <input type='text' id='method' style="width:100px" placeholder="GET/POST"/><br/><br/>
                <div id="app">

                </div>
                <br/>
                <input type='submit'  value='submit'/>
            </form>

            <br/>
            <h3 style="margin:15px;">Response:</h3>  

            <div style="word-wrap: break-word;margin:15px;border:2px #666 solid;min-height: 100px;padding:20px;" id="ans">

            </div>
        </div>
    </body>
</html>
