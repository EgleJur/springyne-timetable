import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import dayjs from "dayjs";
import 'dayjs/locale/lt';
import weekdayPlugin from "dayjs/plugin/weekday";
import objectPlugin from "dayjs/plugin/toObject";
import isTodayPlugin from "dayjs/plugin/isToday";
import './Calendar.css';
import Table from 'react-bootstrap/Table';
import { width } from "@mui/system";


const Calendar = (props) => {
	const now = dayjs().locale('lt');
	const params = useParams();
	const isBetween = require('dayjs/plugin/isBetween')
	dayjs.extend(isBetween)

	// const generateColor = () => {
	// 	const randomColor = Math.floor(Math.random() * 16777215)
	// 	  .toString(16)
	// 	  .padStart(6, '0');
	// 	return `#${randomColor}`;
	//   };

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
		let lessonNr = shedules?.group?.shift?.starts;
		let later = 0;
		result.forEach((less) => {
			//console.log(less.lessonTime + " t " + lessonNr);

			if (less?.lessonTime > lessonNr && later === 0) {
				for (let n = lessonNr; n < less?.lessonTime; n++)
					lessonList.push(
						<tr>{"_"}</tr>
					)
				later++;
				// return;
			}
			if (less?.subject?.name !== subjectName) {

				lessonList.push(
					<tr>{less?.subject?.name}</tr>
				)
				subjectName = less?.subject?.name;

				// 
			}
			else if (less?.teacher?.name !== teacherName) {
				lessonList.push(
					<tr>{less?.teacher?.name}</tr>
				)
				teacherName = less?.teacher?.name;
			}

			else if (less?.room?.name !== room) {
				lessonList.push(
					<tr>{less?.room?.name}</tr>
				)
				room = less?.room?.name;

			}
			else {
				lessonList.push(
					<tr>{"_"}</tr>
				)

			}

			lessonNr++;
		});

		return lessonList
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
		days.push(<th style={{ width: 50 }}>#</th>);
		// for view with weekends i < 7
		for (let i = 0; i < 5; i++) {
			days.push(
				<th class="col">
					{now.weekday(i).format(dateFormat)}
				</th>
			);
		}
		return <thead className="table-light"><tr class="row">{days}</tr></thead>;
	};

	const getHolidays = () => {
		const holidayList = [];
		let starts;
		let ends;
		let currentM = currentMonth.format("MM");
		let currentD = currentMonth.daysInMonth();
		//let curentDate = currentMonth.format('YYYY-MM-DD');

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

	let starts = shedules?.group?.shift?.starts;
	let ends = shedules?.group?.shift?.ends;

	const shiftColumn = () => {
		const shift = [];
		for (let i = starts; i <= ends; i++) {
			shift.push(
				<tr>{i}</tr>
			)
		}
		return <th class="col-shift" style={{ width: 50 }}>{shift}</th>
	};

	const renderCells = () => {
		const rows = [];
		let days = [];
		days.push(
			shiftColumn()
		);

		arrayOfDays.forEach((week, index) => {

			week.dates.forEach((d, i) => {
				// console.log(d.day + " day " + holidayList + " list");
				//add lessons here???
				days.push(

					< th
						className={`col cell ${!d.isCurrentMonth || getHolidays().includes(d.day)
							? "disabled"
							// 
							: d.isCurrentDay ? "selectedDay" : ""
							}`
						}
						key={i}
					>
						<span className="number">{d.day}</span>
						{lesson(d)}

					</th >

				);
			});

			rows.push(
				<tr class="row" key={index}>
					{days}
				</tr>
			);
			days = [];
			days.push(
				shiftColumn()
			);
		});

		return <tbody>{rows}</tbody>;
	};

	// <table className="table table-hover shadow p-3 mb-5 bg-body rounded align-middle">
	//    
	//     <tbody>
	//       {groups.content?.map((group) => (
	//         <tr key={group.id} 
	//         id={group.id}
	//           className={group.deleted ? "text-black-50" : ""}>
	//           <td>{group.name}</td>
	//           <td>{group.program?.name}</td>
	//           <td>{group.groupYear}</td>
	//           <td>{group.students}</td>
	//           <td>{group.deleted ? "Ištrintas" : ""}</td>
	//           <td className="text-end">
	//             <button className="btn btn-outline-primary me-1 my-1 btn-link" title="Žiūrėti">
	//               <Link
	//                 className="nav-link"
	//                 to={"/groups/view/" + group.id}
	//               >
	//                 <VisibilityTwoToneIcon/>
	//               </Link>
	//             </button>

	//             <button
	//               className="btn btn-outline-primary me-1 my-1 btn-link" title="Redaguoti"
	//               disabled={group.deleted}
	//             >
	//               <Link
	//                 className="nav-link"
	//                 to={"/groups/edit/" + group.id}
	//               >
	//                 <EditTwoToneIcon/>
	//               </Link>
	//             </button>

	//             {group.deleted ? (
	//               <button
	//               className="btn btn-outline-secondary me-1 my-1 btn-link" title="Atstatyti"
	//                 onClick={() => restoreGroup(group.id)}
	//               >
	//                 <RestoreTwoToneIcon/>
	//               </button>
	//             ) : (
	//               <button
	//                 className="btn btn-danger me-2 my-1 btn-link" title="Ištrinti"
	//                 onClick={() => deleteGroup(group.id)}
	//               >
	//                 <DeleteTwoToneIcon className="red-icon" />
	//               </button>
	//             )}
	//           </td>
	//         </tr>
	//       ))}
	//     </tbody>
	//     <tfoot className="table-light">
	//       <tr>
	//         <td colSpan={6}>
	//           {groups.totalElements == "0"
	//             ? "Įrašų nerasta"
	//             : `Rasta įrašų: ${groups.totalElements}`}
	//         </td>
	//       </tr>
	//     </tfoot>
	//   </table>

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
				<div>
					<table className="table rounded">
						{renderDays()}
						{renderCells()}
					</table>
				</div>
			</div>
		</div>
	);
};

export default Calendar;