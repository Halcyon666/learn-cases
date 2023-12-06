## how to solve mojibake chinese characters

When you use Spring Boot mvc, provide a api endpoint,

maybe confront mojibake chinese characters.

It's not the problem of Spring Boot, but what the client you use.

result form is below.

| server receive content-type     | client Content-Type             | client Accept-Charset | lib use      | worked?  |
|---------------------------------|---------------------------------|-----------------------|--------------|----------|
| application/json; charset=UTF-8 | application/json                | charset=UTF-8         | RestTemplate | Yes      |
| application/json; charset=UTF-8 | application/json; charset=UTF-8 |                       | RestTemplate | Yes      |
| application/json; charset=UTF-8 | application/json                | charset=UTF-8         | HttpClient   | Yes      |
| application/json; charset=UTF-8 | application/json; charset=UTF-8 |                       | HttpClient   | Yes      |
| text/plain; charset=UTF-8       | application/json                | charset=UTF-8         | RestTemplate | ***No*** |
| text/plain; charset=UTF-8       | application/json; charset=UTF-8 |                       | RestTemplate | Yes      |
| text/plain; charset=UTF-8       | application/json                | charset=UTF-8         | HttpClient   | Yes      |
| text/plain; charset=UTF-8       | application/json; charset=UTF-8 |                       | HttpClient   | Yes      |

finally conclusion about the form , only when you use RestTemplate, and separately set the `Content-Type`

and `Accept-Charset`, the problem will occur.

thanks for your reading.

If you have any problem, feel free to contact with me.

