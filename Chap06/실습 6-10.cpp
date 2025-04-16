#include <iostream>
using namespace std;

class Math {
public:
	static int abs(int a) { return a > 0 ? a : -a; }
	static int max(int a, int b) { return (a > b) ? a : b; }
	static int min(int a, int b) { return (a > b) ? b : a; }
	static int diff(int a, int b) {
		if (a >= b) { return a - b; }
		else {return b - a;	}
	}
};

int main() {
	int pick = 0;
	while (pick != 5) {
		cout << "1.ABS, 2. MAX, 3. MIN, 4. DIFF, 5. QUIT" << endl;
		cout << " 1 ~ 5중 하나를 고르시오." << endl;
		cin >> pick;
		switch(num)
			case (pick == 1)
				cout << "양수로 출력할 수를 입력하시오 >>";
				int a;
				cin >> a;
				cout << Math::abs(a) << endl;
				break;
		
		
			case (pick == 2) 
				cout << "MAX를 비교할 수 2개를 입력하시오 >>";
				int a,b;
				cin >> a >> b;
				cout << Math::max(a, b) << endl;
				break;
		

			case (pick == 3)
				cout << "MIN을 비교할 수 2개를 입력하시오 >>";
				int a, b;
				cin >> a >> b;
				cout << Math::min(a, b) << endl;
				break;

		else if (pick == 4) {
			cout << "두 수의 차를 비교할 수 2개를 입력하시오 >>";
			int a, b;
			cin >> a >> b;
			cout << Math::diff(a, b) << endl;
		}

		else if (pick == 5) {
			cout << "프로그램을 종료합니다." << endl;
		}

		else {
			cout << "잘못된 값 입력" << endl;
			continue;
		}
	}

}

