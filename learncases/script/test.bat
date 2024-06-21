@echo off
setlocal

REM local ecffect env

set LOG_FILE=1.log
echo %DATE% %TIME% before chcp >> %LOG_FILE%

REM console encoding utf-8
chcp 65001

echo %DATE% %TIME% after chcp >> %LOG_FILE%

endlocal
