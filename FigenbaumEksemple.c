#include<stdio.h>
void main()
{
    double lambda =4; 
    double out = 0.1;
    for (int i = 0; i < 500; i++) {
	out = lambda * out * (1 - out);
    }
    for (int i = 0; i < 50; i++) {
	out = lambda * out * (1 - out);
	printf("%f,%f \n", out,lambda);
    }
}
