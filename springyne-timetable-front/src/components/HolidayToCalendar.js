import dayjs from "dayjs";
import 'dayjs/locale/lt';

const HolidayToCalendar = (holidays, currentMonth) => {
	const holidayList = [];
	let starts;
	let ends;
	let currentYear = dayjs(currentMonth).format("YYYY");
	let currentM = dayjs(currentMonth).format("MM");
	let daysInCurrentM = dayjs(currentMonth).daysInMonth();
	console.log(currentYear);
	holidays.forEach((holiday) => {

		let dayStarts = dayjs(holiday.starts).format("D");
		let dayEnds = dayjs(holiday.ends).format("D");
		let monthStarts = dayjs(holiday.starts).format("MM");
		let monthEnds = dayjs(holiday.ends).format("MM");
		let yearEnds = dayjs(holiday.ends).format("YYYY");
		let yearStarts = dayjs(holiday.starts).format("YYYY");
		let holidayName = holiday.name;

		if (monthStarts === currentM && yearStarts === yearEnds) {

			starts = parseInt(dayStarts);

			if (monthEnds === currentM) {
				ends = parseInt(dayEnds);

			} else {
				ends = daysInCurrentM;
			}
		} else if (yearStarts !== yearEnds) {
			if (currentYear === yearStarts) {
				if (currentM === monthStarts) {
					starts = parseInt(dayStarts);
					ends = daysInCurrentM;
				}
			}
			if (currentYear === yearEnds) {
				if (currentM === monthEnds) {
					starts = parseInt(1);
					ends = parseInt(dayEnds);
				}
			}

		}
		else if (dayjs(currentM).isBetween(monthStarts, monthEnds) && monthStarts < monthEnds) {
			starts = parseInt(1); console.log(starts);
			ends = daysInCurrentM; console.log(ends);
		}
		else if (monthEnds === currentM) {
			ends = dayEnds;
			if (monthStarts !== currentM) {
				starts = parseInt(1);
			}
		}

		if (monthStarts === currentM || monthEnds === currentM || dayjs(currentM).isBetween(monthStarts, monthEnds)) {
			//if (monthStarts <= monthEnds){
			for (let i = starts; i <= ends; i++) {
				console.log(i);
				holidayList.push({ day: i, name: holidayName, month: parseInt(currentM) });
				//} 
			// } else{
			// 	for (let i = ends; i <= starts; i++) {
			// 	holidayList.push({ day: i, name: holidayName, month: parseInt(currentM) });
			// 	} 
		}
		}
	});
	return holidayList;
};

export default HolidayToCalendar;