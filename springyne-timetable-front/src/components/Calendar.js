import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import dayjs from "dayjs";
import 'dayjs/locale/lt';
import weekdayPlugin from "dayjs/plugin/weekday";
import objectPlugin from "dayjs/plugin/toObject";
import isTodayPlugin from "dayjs/plugin/isToday";
import './Calendar.css';
import Table from 'react-bootstrap/Table';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import ListItemIcon from '@mui/material/ListItemIcon';
import Divider from '@mui/material/Divider';
import List from '@mui/material/List';



const Calendar = (props) => {
	const now = dayjs().locale('lt');
	const params = useParams();
	const isBetween = require('dayjs/plugin/isBetween')
	dayjs.extend(isBetween)

const colorArray=["#fff4f4", "#f4ffff",
"#fff4fa","#fffaf4","#fffff4","#f4fff4", "#fff4f8","#fbf4ff", "#fcfff0"];
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
		let starts = shedules?.group?.shift?.starts;
		let ends = shedules?.group?.shift?.ends;
		for (let i = starts; i <= ends; i++) {


			shift.push(
				<ListItem disablePadding>
					<ListItemButton sx={{ height: "40px", p:1}}>
						<ListItemText sx={{ fontSize: "0.85rem", m: 0 }} disableTypography primary={i} />
					</ListItemButton>
				</ListItem>
			)}
		return <div className="col-shift cell"><List>{shift}</List></div>
	};

	const [lessons, setLessons] = useState([]);
	const fetchLessons = () => {
		fetch("/api/v1/lessons/schedule/" + params.id)
			.then((response) => response.json())
			.then((jsonResponse) => setLessons(jsonResponse));
	};
	useEffect(() => fetchLessons, []);

	const lesson = (d) => {
		const lessonList = [];

		const result = lessons.filter((e) =>
			dayjs(e?.lessonDate).format('YYYY-MM') === currentMonth.format("YYYY-MM")
			&& parseInt(dayjs(e?.lessonDate).format('D')) === d.day);
		let subjectName = "";
		let teacherName = "";
		let room = "";
		let later = 0;
		let lessonNr = shedules?.group?.shift?.starts;
		result.forEach((less) => {
			let colorId = less?.subject?.id;
			//console.log(colorArray[colorId]);
			if (less?.lessonTime > lessonNr && later === 0) {
				for (let n = lessonNr; n < less?.lessonTime; n++)
					lessonList.push(
						<ListItem disablePadding>
							<ListItemButton sx={{ height: "40px", p:0 }}>
								<ListItemText primary="" />
							</ListItemButton>
						</ListItem>

					)
				later++;
			}
			if (less?.subject?.name !== subjectName) {

				lessonList.push(
					<ListItem disablePadding>
						<ListItemButton sx={{ height: "40px", p:1, bgcolor:colorArray[colorId]}}>
							<ListItemText 
							sx={{ fontSize: "0.85rem", m: 0  }} 
							disableTypography 
							primary={less?.subject?.name} />
						</ListItemButton>
					</ListItem>
				
				)
				subjectName = less?.subject?.name;
				lessonNr++;
			}
			else if (less?.teacher?.name !== teacherName) {
				lessonList.push(
					<ListItem disablePadding>
						<ListItemButton sx={{ height: "40px", p:1, bgcolor: colorArray[colorId] }}>
							<ListItemText 
							sx={{ fontSize: "0.85rem", fontWeight: 300, m: 0 }} 
							disableTypography 
							primary={less?.teacher?.name} />
						</ListItemButton>
					</ListItem>
				)
				teacherName = less?.teacher?.name;
				lessonNr++;
			}
			else if (less?.room?.name !== room) {
				lessonList.push(
					<ListItem disablePadding>
						<ListItemButton sx={{ height: "40px", p:1,  bgcolor: colorArray[colorId] }}>
							<ListItemText 
							sx={{ fontSize: "0.85rem", fontWeight: 300, m: 0 }} 
							disableTypography 
							primary={less?.room?.name} />
						</ListItemButton>
					</ListItem>
				)
				room = less?.room?.name;
			}

			else {
				lessonList.push(
					<ListItem disablePadding>
						<ListItemButton sx={{ height: "40px", p:0, bgcolor: colorArray[colorId] }}>
							<ListItemText primary="" />
						</ListItemButton>
					</ListItem>
					// <div>_</div>
				)

			}
			lessonNr++;
		});
		return <List>{lessonList}</List>
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
		let starts;
		let ends;
		let currentM = currentMonth.format("MM");
		let currentD = currentMonth.daysInMonth();

		holidays.forEach((holiday) => {
			let dayStarts = dayjs(holiday.starts).format("D");
			let dayEnds = dayjs(holiday.ends).format("D");
			let monthStarts = dayjs(holiday.starts).format("MM");
			let monthEnds = dayjs(holiday.ends).format("MM");

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
				days.push(
					< div
						className={`col cell ${!d.isCurrentMonth || getHolidays().includes(d.day)
							? "disabled" : d.isCurrentDay ? "selectedDay" : ""}`}
						key={i}>
						<span style={{zIndex: '5'}} className="number">{d.day}</span>
						{lesson(d)}
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
			year: clonedObject.years,
			month: clonedObject.months,
			day: clonedObject.date,
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