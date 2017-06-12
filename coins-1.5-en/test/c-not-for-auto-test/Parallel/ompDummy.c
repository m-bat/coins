/* Dummy OMP routines */

int printf(char*, ...);

void omp_set_num_threads ( int p) 
{
  printf("\n omp_set_num_threads %d", p);
}
void omp_init_lock ( void * * p) 
{
  printf("\n omp_init_lock ");
}
void omp_set_lock ( void * * p) 
{
  printf("\n omp_set_lock ");
}
void omp_unset_lock ( void * * p) 
{
  printf("\n omp_unset_lock ");
}
void omp_destroy_lock ( void * * p) 
{
  printf("\n omp_destroy_lock ");
}
int omp_get_thread_num ( ) 
{
  printf("\n omp_get_thread_num ");
  return 1;
}
