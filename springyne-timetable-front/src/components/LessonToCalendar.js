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

	const result = lessons.filter((e) =>
		dayjs(e?.lessonDate).format('YYYY-MM') === currentMonth.format("YYYY-MM")
		&& parseInt(dayjs(e?.lessonDate).format('D')) === d.day)
		.sort((a, b) => a.lessonTime > b.lessonTime ? 1 : -1);
	let subjectName = "";
	let teacherName = "";
	let room = "";
	let later = 0;
	let starts = shedules?.group?.shift?.starts;
	let ends = shedules?.group?.shift?.ends;
	result.forEach((less, index) => {
		let colorId = less?.subject?.id;
		//console.log(colorArray[colorId]);
		if (less?.lessonTime > starts && later === 0) {
			for (let n = starts; n < less?.lessonTime; n++)
				lessonList.push(
					<ListItem disablePadding>
						<ListItemButton sx={{ height: "40px", p: 0 }}>
							<ListItemText primary="" />
						</ListItemButton>
					</ListItem>

				)
			later++;
		}
		if (less.subject.id !== result[index - 1]?.subject.id) {
			const lessonId = less.id;
			const subjectId = less.subject.id;
			const teacherId = less.teacher.id;
			const roomId = less.room.id;
			lessonList.push(
				<ListItem disablePadding>
					<LightTooltip title={`${less.teacher.name}\n${less.room.name}`}
					>
						<ListItemButton sx={{ height: "40px", p: 1, bgcolor: colorArray[colorId % 20] }}>
							<ListItemText
								sx={{ fontSize: "0.85rem", m: 0 }}
								disableTypography
								primary={less.subject.name} />
						</ListItemButton>
					</LightTooltip>
					{/* {console.log(lessonId +" id")} */}
					<LongMenu color={colorArray[colorId]}
						lesson={less}
						lessonId={lessonId} subjectId={subjectId}
						teacherId={teacherId} roomId={roomId}
						starts={starts} ends={ends}
						onLessonEdited={onLessonEdited} setSuccess={setSuccess}
						setFailure={setFailure} />
				</ListItem>

			)

			subjectName = less.subject.name;
			starts++;
		}
		else if (less.teacher.name !== teacherName
			&& less.subject.id === result[index - 1].subject.id) {
			lessonList.push(
				<ListItem disablePadding>
					<ListItemButton sx={{ height: "40px", p: 1, bgcolor: colorArray[colorId % 20] }}>
						<ListItemText
							sx={{ fontSize: "0.85rem", fontWeight: 300, m: 0 }}
							disableTypography
							primary={less.teacher.name} />
					</ListItemButton>
				</ListItem>
			)
			teacherName = less.teacher.name;
			starts++;
		}
		else if (less.room.name !== room && less.subject.id === result[index - 1]?.subject.id && less.teacher.id === result[index - 1]?.teacher.id) {
			lessonList.push(
				<ListItem disablePadding>
					<ListItemButton sx={{ height: "40px", p: 1, bgcolor: colorArray[colorId % 20] }}>
						<ListItemText
							sx={{ fontSize: "0.85rem", fontWeight: 300, m: 0 }}
							disableTypography
							primary={less.room.name} />
					</ListItemButton>
				</ListItem>
			)
			room = less.room.name;
		}

		else {
			lessonList.push(
				<ListItem disablePadding>
					<ListItemButton sx={{ height: "40px", p: 0, bgcolor: colorArray[colorId % 20] }}>
						<ListItemText primary="" />
					</ListItemButton>
				</ListItem>
			)
		}
		starts++;
	});
	return <List>{lessonList}</List>
};

export default LessonToCalendar;