<#import "/layouts/mail-layout.ftl" as layouts />

<@layouts.mailLayout>
    <p class="h1" style="Margin:0;font-weight:700;font-size:18px;Margin-bottom:25px;">${address}</p>
    <p style="Margin:0;font-size:16px;Margin-bottom:30px;">${intro}</p>
    <center style="Margin-bottom:30px;"><a href="${confirmationUrl}" target="_blank" style="color:#2b5a83;text-decoration:none;border-radius:3px;border:1px solid #2b5a83;padding: 10px 19px 12px 19px;font-size:17px;font-weight:500;display:inline-block;" >${linkText}</a></center>
    <p style="Margin:0;font-size:16px;Margin-bottom:15px;">${welcome}</p>
    <p style="Margin:0;font-size:16px;Margin-bottom:10px;">${greetings}</p>
</@layouts.mailLayout>
