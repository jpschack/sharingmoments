<#macro mailLayout htmlLanguage="de">
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    <html lang="${htmlLanguage}" xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
            <!--[if !mso]><!-->
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
            <!--<![endif]-->
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title></title>
            <!--[if (gte mso 9)|(IE)]>
            <style type="text/css">
                table {border-collapse: collapse !important;}
            </style>
            <![endif]-->
        </head>
        <body style="padding-top:0;padding-bottom:0;padding-right:0;padding-left:0;margin-top:0 !important;margin-bottom:0 !important;margin-right:0 !important;margin-left:0 !important;background-color:#fff;">
            <center class="wrapper" style="width:100%;table-layout:fixed;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;">
                <div class="webkit" style="max-width:600px;margin-top:0;margin-bottom:0;margin-right:auto;margin-left:auto;">
                    <!--[if (gte mso 9)|(IE)]>
                    <table width="100%" style="border-spacing:0;font-family:sans-serif;color:#333;">
                    <tr>
                    <td width="50%" valign="top" style="padding-top:0;padding-bottom:0;padding-right:0;padding-left:0;">
                    <![endif]-->
                    <table class="outer" align="center" style="border-spacing:0;font-family:sans-serif;color:#333;max-width:600px;Margin:0 auto;width:100%;">
                        <tr>
                            <td style="padding-top:0;padding-bottom:0;padding-right:0;padding-left:0;">
                                <table class="outer" align="center" style="border-spacing:0;font-family:sans-serif;color:#333;max-width:600px;Margin:0 auto;width:100%;">
                                    <tr>
                                        <td class="one-column" style="padding-top:0;padding-bottom:0;padding-right:0;padding-left:0;">
                                            <table width="100%" style="border-spacing:0;font-family:sans-serif;color:#333;">
                                                <tr>
                                                    <td class="inner contents" style="padding-top:10px;padding-bottom:10px;padding-right:10px;padding-left:10px;text-align:left;">
                                                        <#nested />
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                    <!--[if (gte mso 9)|(IE)]>
                    </td>
                    </tr>
                    </table>
                    <![endif]-->
                </div>
            </center>
        </body>
    </html>
</#macro>
