import React from "react";
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrash, faPencilAlt } from "@fortawesome/free-solid-svg-icons";
import { Todo } from "../../types/types";
import "./TodoCard.scss";

dayjs.extend(relativeTime);

interface TodoCardProps {
  todo: Todo;
  onDelete: (id: number) => void;
  onUpdate: (id: number, data: Partial<Todo>) => void;
}

const TodoCard: React.FC<TodoCardProps> = ({ todo, onDelete, onUpdate }) => {
  return (
    <div className={`todo-item ${todo.completed ? "completed" : ""}`}>
      <div>
        <h2>{todo.title}</h2>
        <h4>{dayjs(todo.createdAt).format("h:mm A, MM/DD/YYYY")}</h4>
        <p>{todo.description}</p>
      </div>
      <div>
        <button onClick={() => onUpdate(todo.id, { completed: !todo.completed })} className="edit-btn">
          <FontAwesomeIcon icon={faPencilAlt} />
        </button>
        <button onClick={() => onDelete(todo.id)} className="delete-btn">
          <FontAwesomeIcon icon={faTrash} />
        </button>
      </div>
    </div>
  );
};

export default TodoCard;
