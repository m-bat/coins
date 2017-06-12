int printf(char *, ...);

struct address {
  char* name;
  int zip1;
  int zip2;
};

int main(){
  struct address yamada;

  yamada.name = "Taro Yamada";
  yamada.zip1 = 359;
  yamada.zip2 = 1143;
  
  printf("Name:%s ZipCode:%d-%d\n", yamada.name, yamada.zip1, yamada.zip2);

  return 0;
} 
