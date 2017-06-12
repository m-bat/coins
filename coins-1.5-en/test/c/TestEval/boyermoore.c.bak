/* boyerMoore.c 
   Boyer-Moore string match algorithm (Watanabe)
*/

#include <stdio.h>
#include <stdlib.h>
/*
#include <sys/time.h>
#include <sys/resource.h>
*/
#include <string.h>

#define TEXT_LIMIT 10000
#define SIZE          50
#define CHAR_LIMIT   256
#define ITERATION    100

void InitSkip(int);
void InitNext(int);
int  BmMatch(int, int);
int  max(int, int);
int  min(int, int);

char pattern[SIZE];
char text[TEXT_LIMIT];
int  n;
int  m;
int  skip[CHAR_LIMIT];
int  next[SIZE];

int main()
{
  int  i, k;
  long j;
  double startu, starts, endu, ends;

  strcpy(text, "abcdefabcdefabcdefabcdefabcdef");
  for (i = 30; i < TEXT_LIMIT-30; i = i + 30)
    strcat(&text[i], "abcdefabcdefabcdefabcdefabcdef");
  text[TEXT_LIMIT-32] = '\0';
  strcat(&text[TEXT_LIMIT-32], "abcdefabcdefabcdefabcdefgabcdef");
  printf("text[0-59]=");
  for (i = 0; i < 60; i++)
    printf("%c", text[i]);
  printf("\n");
  printf("text[%d-%d]=", TEXT_LIMIT-32, TEXT_LIMIT-1);
  for (i = TEXT_LIMIT-32; i < TEXT_LIMIT; i++)
    printf("%c", text[i]);
  printf("\n");
  n = strlen(text);
  strcpy(pattern, "gabcdef");
  m = strlen(pattern);
  InitSkip(m);
  InitNext(m);
  for (j = 0L; j < 100; j++) {
    i = BmMatch(m, n);
  }

  if (i < 0)
    printf("not found\n");
  else
    printf("found in %d\n", i);
  return 0;
}


#define TEXT_LIMIT 10000
#define SIZE          50
#define CHAR_LIMIT   256

void InitSkip(int);
void InitNext(int);
int  BmMatch(int, int);
int  max(int, int);
int  min(int, int);

extern char pattern[SIZE];
extern char text[TEXT_LIMIT];
extern int  n;
extern int  m;
extern int  skip[CHAR_LIMIT];
extern int  next[SIZE];

int max(int i, int j) { return ((i < j)? j: i); }

int min(int i, int j) { return ((i < j)? i: j); }

/*--------*/
void InitSkip(int pm)
{
  int j, c;

  for (c = 0; c < CHAR_LIMIT; c++)
    skip[c] = pm;
  for (j = 0; j < pm - 1; j++)
    skip[pattern[j] & 0xff] = pm - 1 - j;
}

/*--------*/
void InitNext(int pm)
{
  int j, k, s;
  int g[SIZE];

  for (j = 0; j < pm; j++)
    next[j] = 2 * pm - 1 - j;
  j = pm;
  for (k = pm - 1; k >= 0; k--) {
    g[k] = j;
    pattern[pm] = pattern[k];
    while (pattern[j] != pattern[k]) {
      next[j] = min(next[j], pm - 1 - k);
      j = g[j];
    }
    j --;
  }
  s = j;
  for (j = 0; j < pm;  j++) {
    next[j] = min(next[j], s + pm - j);
    if (j >= s)
      s = g[s];
  }
}

/*--------*/
int BmMatch(
    int pm,     /* pattern length */
    int pn)     /* text length    */
{
  int i, j;

  i = pm - 1;
  while (i < pn) {
    j = pm - 1;
    while ((j >= 0) && (text[i] == pattern[j])) {
      i--;
      j--;
    }
    if (j < 0)
      return (i + 1);
    i += max(skip[text[i] & 0xff], next[j]);
  }
  return (-1);
}

