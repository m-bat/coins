typedef int Length;

Length    len, maxlen;
Length    *lengths[2];

typedef char *String;

#include <stdlib.h>
#define MAXLINES 100

String f()
{

  String p, lineptr[MAXLINES], alloc(int);
  int strcmp(String, String); /* Conflicting type for built-in function */
                              /* strcmp does not match global one. */
  int strcpy(String, String); /* Conflicting type for built-in function */
  p = (String) malloc(100);
  strcpy(p, "xyz");
  return p;
}

typedef struct tnode *Treeptr;

typedef struct tnode {
  char *word;
  int count;
  Treeptr left;
  Treeptr right;
} Treenode;

Treeptr talloc(void)
{
  return (Treeptr) malloc(sizeof (Treenode));
}

typedef int (*PFI)(char *, char *);

/* PFI strcmp, numcmp; */ /* built-in function strcmp declared as non-function */
PFI numcmp; 

int main()
{
  Treeptr p = talloc();
  Treeptr p2;
  String q;
  p->word = "abc";
  p->count = 1;
  p2 = p;
  q = f();
  lengths[0] = &(p->count);
  printf("%s %s %d %d \n", q, p2->word, p2->count, *lengths[0]);
  return 0;
}
