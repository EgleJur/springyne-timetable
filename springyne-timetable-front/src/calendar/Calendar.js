import { useEffect, useState } from "react";
import dayjs from "dayjs";
import "dayjs/locale/lt";
import weekdayPlugin from "dayjs/plugin/weekday";
import objectPlugin from "dayjs/plugin/toObject";
import isTodayPlugin from "dayjs/plugin/isToday";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemText from "@mui/material/ListItemText";
import List from "@mui/material/List";
import LessonToCalendar from "./LessonToCalendar";
import HolidayToCalendar from "./HolidayToCalendar";
import "./Calendar.css";

const Calendar = (props) => {
  const now = dayjs().locale("lt");
  const isBetween = require("dayjs/plugin/isBetween");
  dayjs.extend(isBetween);
  const lessons = props.lessons;
  const schedule = props.schedule;
  const onLessonEdited = props.onLessonEdited;
  const setSuccess = props.setSuccess;
  const setFailure = props.setFailure;
  const holidays = props.holidays;

  const shift = () => {
    const shift = [];
    let starts = schedule?.group?.shift?.starts;
    let ends = schedule?.group?.shift?.ends;
    for (let i = starts; i <= ends; i++) {
      shift.push(
        <ListItem disablePadding>
          <ListItemButton sx={{ height: "40px", p: 1 }}>
            <ListItemText
              sx={{ fontSize: "0.85rem", m: 0 }}
              disableTypography
              primary={i}
            />
          </ListItemButton>
        </ListItem>
      );
    }
    return (
      <div className="col-shift cell">
        <List>{shift}</List>
      </div>
    );
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
          <span style={{ textTransform: "uppercase" }}>
            {currentMonth.format(dateFormat)}
          </span>
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
    days.push(<div className="col-pam col-center ">#</div>);
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
  const found = (d) => {
    const holiday = HolidayToCalendar(holidays, currentMonth).filter((obj) => {
      return obj.day === d.day;
    });
    if (holiday.length > 0) {
      return holiday.map((holiday) => holiday.name);
    } else {
      return null;
    }
  };

  useEffect(() => {
    getAllDays();
  }, [currentMonth]);

  const renderCells = () => {
    const rows = [];
    let days = [];
    days.push(shift());

    arrayOfDays.forEach((week, index) => {
      week.dates.forEach((d, i) => {
        days.push(
          <div
            className={`col cell ${!d.isCurrentMonth ||
              HolidayToCalendar(holidays, currentMonth).some(
                (index) => index.day === d.day
              )
              ? "disabled"
              : ""
              }`}
            key={i}
          >
            <span style={{ zIndex: "5" }} className="number">
              {d.day}
            </span>
            {d.isCurrentMonth && found(d) && (
              <div className="mx-3 text-center">
                {found(d).map((name) => (
                  <div className="my-3">{name}</div>
                ))}
              </div>
            )}

            {d.isCurrentMonth &&
              LessonToCalendar(
                d,
                schedule,
                lessons,
                currentMonth,
                onLessonEdited,
                setSuccess,
                setFailure
              )}
          </div>
        );
      });

      rows.push(
        <div className="row" key={index}>
          {days}
        </div>
      );
      days = [];
      days.push(shift());
    });

    return <div className="body">{rows}</div>;
  };

  const formateDateObject = (date) => {
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
