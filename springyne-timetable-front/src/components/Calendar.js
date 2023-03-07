import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import dayjs from "dayjs";
import 'dayjs/locale/lt';
import weekdayPlugin from "dayjs/plugin/weekday";
import objectPlugin from "dayjs/plugin/toObject";
import isTodayPlugin from "dayjs/plugin/isToday";
import './Calendar.css';


const Calendar = () => {
	const now = dayjs().locale('lt');

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
		days.push(<div className="col col-center">Pamokos</div>);
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

	const getAllDays = () => {
		let currentDate = currentMonth.startOf("month").weekday(0);
		const nextMonth = currentMonth.add(1, "month").month();

		let allDates = [];
		let weekDates = [];
		//weekDates.push({ dates: null });

		let weekCounter = 1;

		while (currentDate.weekday(0).toObject().months !== nextMonth) {
			const formated = formateDateObject(currentDate);

			weekDates.push(formated);
			// for view with if is not needed

			// for view with weekends weekCounter ===7
			if (weekCounter === 5) {
				allDates.push({ dates: weekDates });
				weekDates = [];
				//weekDates.push({ dates: null });
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
			<div className="col cell">
				
				<td><tr>1</tr>
					<tr>2</tr>
					<tr>3</tr>
					<tr>4</tr></td>
			</div>
		);

		arrayOfDays.forEach((week, index) => {
	
			week.dates.forEach((d, i) => {
			
				days.push(
					
					<div
						className={`col cell ${!d.isCurrentMonth ? "disabled" : d.isCurrentDay ? "selected" : ""
							}`}
						key={i}
					>
						<span className="number">{d.day}</span>
						{/* <span className="bg">{d.day}</span> */}
					</div>
				);
			});
			rows.push(
				<div className="row" key={index}>
					{days}
				</div>
			);
			days = [];
			days.push(
				<div className="col cell">
					<td><tr>1</tr>
					<tr>2</tr>
					<tr>3</tr>
					<tr>4</tr></td>
				</div>
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