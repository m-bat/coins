int printf(char *s, ...);

int main() {
  char *a="'\'\"\?\\\a\b\f\n\r\t\v";
  char *b="\1\12\123\1234\56" "7\78";
  char *c="\x1\x12\x012\xAB\xC" "D\xFG";
  printf("a:");
  while(*a!=0) printf(" %02X",(*a++)&0xFF);
  printf("\nb:");
  while(*b!=0) printf(" %02X",(*b++)&0xFF);
  printf("\nc:");
  while(*c!=0) printf(" %02X",(*c++)&0xFF);
  printf("\n");
  return 0;
}
