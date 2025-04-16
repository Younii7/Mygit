#include <iostream>
using namespace std;

class Power {
	int kick;
	int punch;
public:
	Power(int kick = 0, int punch = 0) {
		this->kick = kick; this->punch = punch;
	}
	void show();
	Power& operator+= (Power op2); // += 연산자 함수 선언
	Power& operator-= (Power op2);

};

void Power::show() {
	cout << "kick=" << kick << ',' << "punch=" << punch
		<< endl;
}

Power& Power::operator+=(Power op2) {
	kick = kick + op2.kick; // kick 더하기
	punch = punch + op2.punch; // punch 더하기
	return *this; // 합한 결과 리턴
}

Power& Power::operator-=(Power op2) {
	kick = kick - op2.kick;
	punch = punch - op2.punch;
	return *this;
}

int main() {
	int ak, ap, bk, bp;
	cout << "a의 kick과 power의 값을 입력하시오 >> ";
	cin >> ak >> ap;
	cout << "b의 kick과 power의 값을 입력하시오 >> ";
	cin >> bk >> bp;
	Power a(ak, ap), b(bk, bp), c;
	a.show();
	b.show();
	c = a += b; // 파워 객체 더하기
	a.show();
	c.show();
	c = a -= b;
	a.show();
	c.show();
}