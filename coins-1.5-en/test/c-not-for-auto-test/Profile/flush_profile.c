#include <stdio.h>
#include <stdlib.h>

#define COUNTER_TYPE int

#include "profile_data.c"

/*
 * extern char *__counter_names[];
 * extern COUNTER_TYPE *__counter_vectors[];
 * extern int __countersizes[];
 */

static char *filename = "profile_data";

void __flush_profile()
{
  int i, j;
  FILE *fp;

  if ((fp = fopen(filename, "w")) == NULL) {
    fprintf(stderr, "can't open: %s\n", filename);
    exit(1);
  }
  for (i = 0; __countersizes[i] >= 0; i++) {
    fprintf(fp, "# %s\n", __counter_names[i]);
    for (j = 0; j < __countersizes[i]; j++) {
      fprintf(fp, "%d\n", __counter_vectors[i][j]);
    }
  }
  fclose(fp);
}
