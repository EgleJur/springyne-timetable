import dayjs from "dayjs";
import 'dayjs/locale/lt';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import List from '@mui/material/List';
import LongMenu from "./LongMenu";
import { styled } from '@mui/material/styles';
import Tooltip, { tooltipClasses } from '@mui/material/Tooltip';

const LessonToCalendar = (d, shedules, lessons, currentMonth, onLessonEdited, setSuccess, setFailure) => {
	const colorArray = ["#fff0f4", "#fff4f8", "#fffff4", "#fff8f4", "#f4fff8",
		"#fffaf4", "#fff4fa", "#fffaf0", "#fcfff0", "#fbf4ff",
		"#f0fffa", "#fff4f4", "#f4fff4", "#f4f8ff", "#f4f4ff",
		"#f0fff4", "#fff4f0", "#f4ffef", "#f4ffff", "#f8fff4"];

	const LightTooltip = styled(({ className, ...props }) => (
		<Tooltip {...props} classes={{ popper: className }} />
	))(({ theme }) => ({
		[`& .${tooltipClasses.tooltip}`]: {
			backgroundColor: theme.palette.common.white,
			color: 'rgba(0, 0, 0, 0.87)',
			boxShadow: theme.shadows[1],
			fontSize: 11,
		},
	}));

	const lessonList = [];

	let starts = shedules?.group?.shift?.starts;
	let ends = shedules?.group?.shift?.ends;

	// Create a new array with all time values between start and end
	const allTimes = Array.from({length: ends - starts + 1}, (_, i) => i + starts);

	allTimes.forEach(time => {
		// Check if there is a lesson for this time value in the original list of lessons
		const lesson = lessons.find(less => parseInt(dayjs(less?.lessonDate).format('D')) === d.day && less?.lessonTime === time);

		if (!lesson) {
			// If there is no lesson for this time value, add an empty lesson to the list
			lessonList.push(
				<ListItem disablePadding>
					<ListItemButton sx={{ height: "40px", p: 0 }}>
						<ListItemText primary="" />
					</ListItemButton>
				</ListItem>
			);
		} else {
			// If there is a lesson for this time value, add it to the list
			let colorId = lesson?.subject?.id;
			if (lesson?.lessonTime > starts) {
				for (let n = starts; n < lesson?.lessonTime; n++)
					lessonList.push(
						<ListItem disablePadding>
							<ListItemButton sx={{ height: "40px", p: 0 }}>
								<ListItemText primary="" />
							</ListItemButton>
						</ListItem>
					);
			}

			const lessonId = lesson.id;
			const subjectId = lesson.subject.id;
			const teacherId = lesson.teacher.id;
			const roomId = lesson.room.id;

			lessonList.push(
				<ListItem disablePadding sx={{ backgroundColor: colorArray[colorId % colorArray.length] }}>
				<ListItemButton sx={{ height: "40px", p: 0 }}>
				<LightTooltip title={lesson?.subject?.name} placement="top">
				<ListItemText
				primary={lesson?.lessonTime}
				onClick={() => {
				LongMenu(lessonId, subjectId, teacherId, roomId, onLessonEdited, setSuccess, setFailure);
				}}
				/>
				</LightTooltip>
				</ListItemButton>
				</ListItem>
				);
				}
				});

	
	return <List>{lessonList}</List>
};

export default LessonToCalendar;