import axios from "axios";
import React, { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

export default function ViewTeacher() {
const [teacher, setTeacher] = useState({
name: "",
lastName: "",
email: "",
});

//const params = useParams();
const { id } = useParams();

useEffect(() => {
loadTeacher();
}, [id]);

const loadTeacher = async () => {
const result = await axios.get(`http://localhost:8080/teacher/${id}`);
setTeacher(result.data);
};

return (
<div className="container">
<div className="row">
<div className="col-md-6 offset-md-3 border rounded p-4 mt-2 shadow">
<h2 className="text-center m-4">Teacher Details</h2>
<div className="card">
<div className="card-header">
Details of teacher id: {teacher.id}
<ul className="list-group list-group-flush">
<li className="list-group-item">
<b>Name:</b> {teacher.name}
</li>
<li className="list-group-item">
<b>Last Name:</b> {teacher.lastName}
</li>
<li className="list-group-item">
<b>Email:</b> {teacher.email}
</li>
</ul>
</div>
</div>
<Link className="btn btn-primary my-2" to={"/"}>
Go Back
</Link>
</div>
</div>
</div>
);
}