import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import dayjs from "dayjs";
import 'dayjs/locale/lt';
import weekdayPlugin from "dayjs/plugin/weekday";
import objectPlugin from "dayjs/plugin/toObject";
import isTodayPlugin from "dayjs/plugin/isToday";
import './Calendar.css';


const Calendar = (props) => {
	const now = dayjs().locale('lt');
const params = useParams();
	const isBetween = require('dayjs/plugin/isBetween')
	dayjs.extend(isBetween)


	const [holidays, setHolidays] = useState([]);

	const fetchHolidays = () => {
		fetch(`/api/v1/holidays/search?name=${""}&from=${""}&to=${""}`)
			.then((response) => response.json())
			.then((jsonResponse) => setHolidays(jsonResponse));
	};
	useEffect(() => fetchHolidays, []);

	
	const [shedules, setShedules] = useState([]);
	const fetchShedules = () => {
		fetch("/api/v1/schedules/" + params.id)
			.then((response) => response.json())
			.then((jsonResponse) => setShedules(jsonResponse));
	};
	useEffect(() => fetchShedules, []);

	const shift = () => {
		const shift = [];
		var starts = shedules?.group?.shift?.starts;
		var ends = shedules?.group?.shift?.ends;
		for (var i = starts; i <= ends; i++) {
			shift.push(
				<div>{i}</div>
			)

		}
		return <div className="col-shift cell">{shift}</div>
	};

	const [lessons, setLessons] = useState([]);
	const fetchLessons = () => {
		fetch("/api/v1/lessons/" + params.id)
			.then((response) => response.json())
			.then((jsonResponse) => setLessons(jsonResponse));
	};
	useEffect(() => fetchLessons, []);

	const lesson = (d) => {
		const lessonList = [];
		lessons.filter((e) => dayjs(e.lessonDate).format('YYYY-MM-DD') === d)
		.forEach((les)=>{
			lessonList.push(
			<div>{les.lessonTime}</div>
			)
		});
		
		return <div /*className="col-shift cell"*/>{lessonList}</div>
	};
	

	dayjs.extend(weekdayPlugin);
	dayjs.extend(objectPlugin);
	dayjs.extend(isTodayPlugin);

	const [currentMonth, setCurrentMonth] = useState(now);
	const [arrayOfDays, setArrayOfDays] = useState([]);

	const nextMonth = () => {
		const plus = currentMonth.add(1, "month");

		setCurrentMonth(plus);
	};

	const prevMonth = () => {
		const minus = currentMonth.subtract(1, "month");

		setCurrentMonth(minus);
	};

	const renderHeader = () => {
		const dateFormat = "MMMM YYYY";

		return (
			<div className="header row flex-middle">
				<div className="col col-start">
					<div className="icon" onClick={() => prevMonth()}>
						chevron_left
					</div>
				</div>
				<div className="col col-center">
					<span>{currentMonth.format(dateFormat)}</span>
				</div>
				<div className="col col-end" onClick={() => nextMonth()}>
					<div className="icon">chevron_right</div>
				</div>
			</div>
		);
	};

	const renderDays = () => {
		const dateFormat = "dddd";
		const days = [];
		days.push(<div className="col-pam col-center " >#</div>);
		// for view with weekends i < 7
		for (let i = 0; i < 5; i++) {
			days.push(
				<div className="col col-center" key={i}>
					{now.weekday(i).format(dateFormat)}
				</div>
			);
		}
		return <div className="days row">{days}</div>;
	};

	const getHolidays = () => {
		const holidayList = [];
		var starts;
		var ends;
		var currentM = currentMonth.format("MM");
		var currentD = currentMonth.daysInMonth();
		//let curentDate = currentMonth.format('YYYY-MM-DD');

		holidays.forEach((holiday) => {
			var dayStarts = dayjs(holiday.starts).format("D");
			var dayEnds = dayjs(holiday.ends).format("D");
			var monthStarts = dayjs(holiday.starts).format("MM");
			var monthEnds = dayjs(holiday.ends).format("MM");

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
				for (var i = starts; i <= ends; i++) {
					holidayList.push(i);
				}
			}
		});
		return holidayList;
	};


	const getAllDays = () => {
		let currentDate = currentMonth.startOf("month").weekday(0);
		const nextMonth = currentMonth.add(1, "month").month();

		let allDates = [];
		let weekDates = [];

		let weekCounter = 1;

		while (currentDate.weekday(0).toObject().months !== nextMonth) {
			const formated = formateDateObject(currentDate);

			weekDates.push(formated);

			// for view with weekends weekCounter ===7
			if (weekCounter === 5) {
				allDates.push({ dates: weekDates });
				weekDates = [];
				weekCounter = 0;
			}


			if (weekCounter === 0) {
				currentDate = currentDate.add(2, "day");
			}
			weekCounter++;
			currentDate = currentDate.add(1, "day");
		}

		setArrayOfDays(allDates);
	};

	useEffect(() => {
		getAllDays();
	}, [currentMonth]);

	const renderCells = () => {
		const rows = [];
		let days = [];
		days.push(
			shift()
		);

		arrayOfDays.forEach((week, index) => {

			week.dates.forEach((d, i) => {
				// console.log(d.day + " day " + holidayList + " list");
				//add lessons here???
				days.push(

					< div
						className={`col cell ${!d.isCurrentMonth || getHolidays().includes(d.day)
							? "disabled"
							// 
							: d.isCurrentDay ? "selected" : ""
							}`
						}
						key={i}
					>
						<span className="number">{d.day}</span>
						{lesson(d.day)}

						{/* <span className="bg">{d.day}</span> */}
					</div >

				);
			});

			rows.push(
				<div className="row" key={index}>
					{days}
				</div>
			);
			days = [];
			days.push(
				shift()
			);
		});

		return <div className="body">{rows}</div>;
	};

	const formateDateObject = date => {
		const clonedObject = { ...date.toObject() };

		const formatedObject = {
			day: clonedObject.date,
			month: clonedObject.months,
			year: clonedObject.years,
			isCurrentMonth: clonedObject.months === currentMonth.month(),
			isCurrentDay: date.isToday(),
		};

		return formatedObject;
	};

	return (
		<div>

			<div className="calendar">
				{renderHeader()}
				{renderDays()}
				{renderCells()}
			</div>
		</div>
	);
};

export default Calendar;