if #%OS%==#Windows_NT rd /S /Q classes
if not #%OS%==#Windows_NT deltree /Y classes
if not exist classes mkdir classes
chdir src
javac -d ..\classes @files
chdir ..
copy suffixes classes\coins\driver
