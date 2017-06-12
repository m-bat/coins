int printf(char *s, ...);

int main() {
  int i;
  for(i=0;i<15;i++) {
    switch(i) {
    case 2:
    case 7:
    case 12:
      if(i%5!=2) printf("bad: %d%%5!=2\n",i);
      break;
    default:
      if(i%5==2) printf("bad: %d%%5==2\n",i);
      break;
    }
  }
  printf("Switch test\n");
  return 0;
}
