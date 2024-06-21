@echo off
setlocal

REM set local log filename
set LOG_FILE=1.log

REM not mojibake, call test set encoding to UTF-8
echo %DATE% %TIME% after chcp >> %LOG_FILE%

endlocal
