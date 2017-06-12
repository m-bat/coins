/* tpstruct1.c:  Simple struct */

int a, b, c;
struct address {
  char* name;
  int   zip;
};
struct address tanaka;

int main()
{
  tanaka.name = "Tanaka";
  tanaka.zip = 184;
  if (tanaka.zip == 0)
    tanaka.zip = 001;
  printf("tanaka = [%s,%d]\n",tanaka.name,tanaka.zip); /* SF030609 */
  return 0;
}

