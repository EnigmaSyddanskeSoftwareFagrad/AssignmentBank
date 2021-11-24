@echo off
:beginning
start /MIN "Test Client" TestClient.exe
pause
timeout 60
GOTO beginning