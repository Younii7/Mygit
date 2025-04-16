#include <iostream>
using namespace std;

// 원형선언
void f(char c = 'X', int count = 5);
// 함수구현

void f(char c, int count) {
	for (int i = 0; i < count; i++) {
		cout << c;
	}
	cout << endl;
}
int main() {
	f(); // 한줄에빈칸을10개출력한다.
	f('%'); // 한줄에'%'를10개출력한다.
	f('A', 3); // 5 줄에'@' 문자를10개출력한다.
}
