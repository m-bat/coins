int printf(char *, ...);

struct address{
  char *name;
  long zip;
  char *address;
};

int main(int args, char *argv[]){
  struct address address1, addressp;
  
  address1.name = "Taro Yamada";
  address1.zip = 3591143;
  address1.address = "Tokyo-to";
  
  addressp = address1;
  printf("Name:%s\n", addressp.name);
  printf("Zip:%d\n", addressp.zip);
  printf("Address:%s\n", addressp.address);

  return 0;
}