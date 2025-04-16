#include <iostream>
using namespace std;

int sum(int a, int b) { // a에서b까지합하기
	int s = 0;
	if (a > b) {
		int tmp = a;
		a = b;
		b = tmp;
	}
	for (int i = a; i <= b; i++)
		s += i;
	return s;
}

int sum(int a) { // 0에서a까지합하기
	int s = 0;
	for (int i = 0; i <= a; i++)
		s += i;
	return s;
}

int main() {
	cout << sum(5, 3) << endl;
	cout << sum(3) << endl;
	cout << sum(100) << endl;
}