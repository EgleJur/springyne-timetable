import dayjs from "dayjs";
import 'dayjs/locale/lt';

const HolidayToCalendar = (holidays, currentMonth) => {
	const holidayList = [];
	let starts;
	let ends;
	let currentYear = dayjs(currentMonth).format("YYYY");
	let currentM = dayjs(currentMonth).format("MM");
	let daysInCurrentM = dayjs(currentMonth).daysInMonth();
	// console.log(currentYear);

	holidays.forEach((holiday) => {

		let dayStarts = dayjs(holiday.starts).format("D");
		let dayEnds = dayjs(holiday.ends).format("D");
		let monthStarts = dayjs(holiday.starts).format("MM");
		let monthEnds = dayjs(holiday.ends).format("MM");
		let yearEnds = dayjs(holiday.ends).format("YYYY");
		let yearStarts = dayjs(holiday.starts).format("YYYY");
		let holidayName = holiday.name;
		const holidayStarts = dayjs(holiday.starts);
		const holidayEnds = dayjs(holiday.ends);
		let addedHoliday = false;

		if (monthStarts === currentM &&
			!holidayList.some(
				(h) =>
					h.name === holidayName &&
					h.day >= parseInt(dayStarts) &&
					h.day <= parseInt(daysInCurrentM)
			)) {
			// console.log(holidayList);
			// console.log(parseInt(dayStarts) + " " + parseInt(daysInCurrentM));
			starts = parseInt(dayStarts);
			if (monthEnds === currentM) {
				ends = parseInt(dayEnds);
			} else {
				ends = daysInCurrentM;
			}
			for (let i = starts; i <= ends; i++) {
				holidayList.push({ day: i, name: holidayName, month: parseInt(currentM) });
			}
		} else if (holidayStarts.isBefore(currentMonth, "MM") && holidayEnds.isAfter(currentMonth, 'MM')) {
			//console.log(holidayList);
			starts = parseInt(1); //console.log(starts);
			ends = daysInCurrentM; //console.log(ends);

			for (let i = starts; i <= ends; i++) {
				holidayList.push({ day: i, name: holidayName, month: parseInt(currentM) });
			}
		}
		else if (monthEnds === currentM) {
			//console.log(holidayList);
			ends = dayEnds;
			if (monthStarts <= currentM) {
				starts = parseInt(1);
			}
			for (let i = starts; i <= ends; i++) {
				holidayList.push({ day: i, name: holidayName, month: parseInt(currentM) });
			}
		}
	});
	return holidayList;
};

export default HolidayToCalendar;