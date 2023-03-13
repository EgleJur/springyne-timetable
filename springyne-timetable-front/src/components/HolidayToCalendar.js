import dayjs from "dayjs";
import 'dayjs/locale/lt';

const HolidayToCalendar = (holidays, currentMonth) => {
		const holidayList = [];
		let starts;
		let ends;
		let currentM = dayjs(currentMonth).format("MM");
		let currentD = dayjs(currentMonth).daysInMonth();

		holidays.forEach((holiday) => {
			let dayStarts = dayjs(holiday.starts).format("D");
			let dayEnds = dayjs(holiday.ends).format("D");
			let monthStarts = dayjs(holiday.starts).format("MM");
			let monthEnds = dayjs(holiday.ends).format("MM");
			let holidayName = holiday.name;

			if (monthStarts === currentM) {
				starts = parseInt(dayStarts);
				if (monthEnds === currentM) {
					ends = parseInt(dayEnds);
				} else {
					ends = currentD;
				}
			}
			else if (dayjs(currentM).isBetween(monthStarts, monthEnds)) {
				starts = parseInt(1);
				ends = currentD;
			}
			else if (monthEnds === currentM) {
				ends = currentD;
				if (monthStarts !== currentM) {
					starts = parseInt(1);
				}
			}

			if (monthStarts === currentM || monthEnds === currentM || dayjs(currentM).isBetween(monthStarts, monthEnds)) {
				for (let i = starts; i <= ends; i++) {
					holidayList.push({ day:i, name: holidayName, month: parseInt(currentM)});
					 
				}
			}
		});
		//console.log(holidayList);
		return holidayList;
	};

    export default HolidayToCalendar;