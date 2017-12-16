
//int algo(int*);
int main(){
  int a,b;
  printf("Ingrese un numero: ");
  scanf("%d",&a);
  while(a > 0){
    printf("a=%d\n", a);
    a--;
  }
  algo(&a);
  for(b=0; b<3; b++){
    printf("for_a=%d\n",a);
  }
  if (a == 3 && b == 2 ) {
    printf("Just as expected\n");
  }else{
    printf("WHAT THE HELL\n");
  }
  printf("a=%d\n", a);
  printf("b=%d\n", b);
  return 0;
}

int algo(int* x, int y, int z){
  *x=3;
  return 0;
}
int valor(int x){
  int h,a,r;
  x=3;
  return 0;
}