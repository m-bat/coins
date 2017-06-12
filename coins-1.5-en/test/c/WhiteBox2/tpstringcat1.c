/* tpstringcat.c: String constant concatination 1 */ 

char *strg1 = "ab" "cd";
int main()
{
  char *str1 = "ef" "g";
  char *str2;
  str2 = "h" "i" "j";
  printf("%s %s %s \n", strg1, str1, str2); 
  return 0;
} 

