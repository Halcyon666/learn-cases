@echo off
setlocal

REM local ecffect env

REM set local log filename
set LOG_FILE=1.log

REM mojibake here, text write with GBK
echo %DATE% %TIME% before chcp >> %LOG_FILE%

REM console encoding utf-8
chcp 65001

echo %DATE% %TIME% after chcp >> %LOG_FILE%

REM console encoding utf-8
REM call test1.bat

endlocal
