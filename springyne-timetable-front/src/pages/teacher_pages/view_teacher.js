import axios from "axios";
import React, { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

export default function ViewTeacher() {
  const [teacher, setTeacher] = useState({
    name: "",
    username: "",
    email: "",
  });

  const { id } = useParams();

  useEffect(() => {
    loadTeacher();
  }, []);

  const loadTeacher = async () => {
    const result = await axios.get(`http://localhost:8080/teacher/${id}`);
    setTeacher(result.data);
  };

  return (
    <div className="container">
      <div className="row">
        <div className="col-md-6 offset-md-3 border rounded p-4 mt-2 shadow">
          <h2 className="text-center m-4">Informacija</h2>

          <div className="card">
            <div className="card-header">
              Details of teacher id : {teacher.id}
              <ul className="list-group list-group-flush">
                <li className="list-group-item">
                  <b>Vardas:</b>
                  {teacher.name}
                </li>
                <li className="list-group-item">
                  <b>Pavardė:</b>
                  {teacher.username}
                </li>
                <li className="list-group-item">
                  <b>Email:</b>
                  {teacher.email}
                </li>
              </ul>
            </div>
          </div>
          <Link className="btn btn-primary my-2" to={"/"}>
            Grižti
          </Link>
        </div>
      </div>
    </div>
  );
}
