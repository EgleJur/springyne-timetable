import dayjs from "dayjs";
import 'dayjs/locale/lt';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import List from '@mui/material/List';

const LessonToCalendar = (d, shedules, lessons, currentMonth) => {
    const colorArray = ["#fff4f4", "#f4ffff",
    "#fff4fa", "#fffaf4", "#fffff4", "#f4fff4", "#fff4f8", "#fbf4ff", "#fcfff0"];

    const lessonList = [];

		const result = lessons.filter((e) =>
			dayjs(e?.lessonDate).format('YYYY-MM') === currentMonth.format("YYYY-MM")
			&& parseInt(dayjs(e?.lessonDate).format('D')) === d.day)
			.sort((a, b) => a.lessonTime > b.lessonTime ? 1 : -1);
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
							<ListItemButton sx={{ height: "40px", p: 0 }}>
								<ListItemText primary="" />
							</ListItemButton>
						</ListItem>

					)
				later++;
			}
			if (less?.subject?.name !== subjectName) {

				lessonList.push(
					<ListItem disablePadding>
						<ListItemButton sx={{ height: "40px", p: 1, bgcolor: colorArray[colorId] }}>
							<ListItemText
								sx={{ fontSize: "0.85rem", m: 0 }}
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
						<ListItemButton sx={{ height: "40px", p: 1, bgcolor: colorArray[colorId] }}>
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
						<ListItemButton sx={{ height: "40px", p: 1, bgcolor: colorArray[colorId] }}>
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
						<ListItemButton sx={{ height: "40px", p: 0, bgcolor: colorArray[colorId] }}>
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

    export default LessonToCalendar;