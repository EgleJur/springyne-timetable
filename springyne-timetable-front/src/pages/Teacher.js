import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useParams } from "react-router-dom";

export default function Teacher() {
  const [teachers, setTeachers] = useState([]);

  const { id } = useParams();

  useEffect(() => {
    loadTeachers();
  }, []);

  const loadTeachers = async () => {
    const result = await axios.get("http://localhost:8080/teachers");
    setTeachers(result.data);
  };

  const deleteTeacher = async (id) => {
    await axios.delete(`http://localhost:8080/teacher/${id}`);
    loadTeachers();
  };

  return (
    <div className="container">
      <div className="py-4">
        <p>
      <button className="btn btn-outline-info">
      <Link className="add_link" style={{textDecoration: "none"}} to="/add_teacher">Prideti Mokytoja</Link>
    </button>
    
    </p>
        <table className="table border shadow">
          <thead>
            <tr>
              <th scope="col">Nr.</th>
              <th scope="col">Vardas</th>
              <th scope="col">Pavardė</th>
              <th scope="col">Email</th>
              <th scope="col">Veiksmai</th>
            </tr>
          </thead>
          <tbody>
            {teachers.map((teacher, index) => (
              <tr>
                <th scope="row" key={index}>
                  {index + 1}
                </th>
                <td>{teacher.name}</td>
                <td>{teacher.username}</td>
                <td>{teacher.email}</td>
                <td>
                  <Link
                    className="btn btn-primary mx-2"
                    to={`/ViewTeacher/${teacher.id}`}
                  >
                    View
                  </Link>
                  <Link
                    className="btn btn-outline-primary mx-2"
                    to={`/Redaguoti/${teacher.id}`}
                  >
                    Edit
                  </Link>
                  <button
                    className="btn btn-danger mx-2"
                    onClick={() => deleteTeacher(teacher.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
