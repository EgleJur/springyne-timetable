import dayjs from "dayjs";
import 'dayjs/locale/lt';

const HolidayToCalendar = (holidays, currentMonth) => {
	const holidayList = [];
	let starts;
	let ends;
	let currentM = dayjs(currentMonth).format("MM");
	let daysInCurrentM = dayjs(currentMonth).daysInMonth();

	holidays.forEach((holiday) => {

		let dayStarts = dayjs(holiday.starts).format("D");
		let dayEnds = dayjs(holiday.ends).format("D");
		let monthStarts = dayjs(holiday.starts).format("MM");
		let monthEnds = dayjs(holiday.ends).format("MM");
		let holidayName = holiday.name;
		const holidayStarts = dayjs(holiday.starts);
		const holidayEnds = dayjs(holiday.ends);

		if (monthStarts === currentM &&
			!holidayList.some(
				(h) =>
					h.name === holidayName &&
					h.day >= parseInt(dayStarts) &&
					h.day <= parseInt(daysInCurrentM)
			)) {
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
			starts = parseInt(1);
			ends = daysInCurrentM;

			for (let i = starts; i <= ends; i++) {
				holidayList.push({ day: i, name: holidayName, month: parseInt(currentM) });
			}
		}
		else if (monthEnds === currentM) {
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