#define	EXIT_FAILURE	1
#define	EXIT_SUCCESS	0
#define	RAND_MAX	0x7fffffff

  typedef struct 
  {
    long int quot;
    long int rem;
  } ldiv_t;

  typedef struct
  {
    int quot;
    int rem;
  } div_t; 

 double atof(const char*); 
 int atoi(const char*); 
 long int atol(const char*); 
 double strtod(const char*, char**); 
 float strtof(const char*, char**); 
 long int strtol(const char*, char**, int); 
 unsigned long int strtoul(const char*, char**, int);
 int rand(void); 
 void srand(unsigned int); 
/* void* calloc(size_t, size_t); */
 void free(void*); 
/* void* malloc(size_t);*/
 void* realloc(void *, size_t);
 void abort(void); 
 int atexit(void (*func)(void)); 
 void exit(int); 
 void _Exit(int); 
 char*getenv(const char*); 
 int system(const char*); 
 void* bsearch(const void*, const void*, size_t, size_t, 
			   int (*comp)(const void*, const void*)); 
 void qsort(void*, size_t, size_t, 
			int (*comp)(const void*, const void*)); 
 int abs(int); 
 long int labs(long int); 
 div_t div(int, int); 
 ldiv_t ldiv(long int, long int); 
 int mblen(const char*, size_t); 
/* int mbtowc(wchar_t*, const char*, size_t); 
 int wctomb(char*, wchar_t); 
 size_t mbstowcs(wchar_t*, const char*, size_t); 
 size_t wcstombs(char*, const wchar_t*, size_t);
*/
 long long int atoll(const char*); 
 long long int strtoll(const char*, char**, int); 
 unsigned long long int strtoull(const char*, char**, int); 
