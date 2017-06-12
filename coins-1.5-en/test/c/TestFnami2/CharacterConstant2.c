int printf(char *s, ...);

int main() {
  char a[]={ '"','?','\'','\"','\?','\\','\a','\b','\f','\n','\r','\t','\v' };
  char b[]={ '\1','\12','\123' };
  char c[]={ '\x1','\x12','\x012','\xAB' };
  int i;
  printf("a:");
  for(i=0;i<sizeof a;i++) printf(" %02X",a[i]&0xFF);
  printf("\nb:");
  for(i=0;i<sizeof b;i++) printf(" %02X",b[i]&0xFF);
  printf("\nc:");
  for(i=0;i<sizeof c;i++) printf(" %02X",c[i]&0xFF);
  printf("\n");
  return 0;
}
