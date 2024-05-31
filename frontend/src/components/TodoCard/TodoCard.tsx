// TodoCard.tsx

import React, { useState } from "react";
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrash, faPencilAlt, faStar } from "@fortawesome/free-solid-svg-icons";
import { Todo } from "../../types/types";
import Modal from "../Modal/Modal";
import "./TodoCard.scss";

dayjs.extend(relativeTime);

interface TodoCardProps {
  todo: Todo;
  onDelete: (id: number) => void;
  onUpdate: (id: number, data: Partial<Todo>) => void;
}

const TodoCard: React.FC<TodoCardProps> = ({ todo, onDelete, onUpdate }) => {
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);

  const handleEditSave = (title: string, description: string) => {
    onUpdate(todo.id, { title, description });
    setIsEditModalOpen(false);
  };

  const handleToggleCompleted = () => {
    onUpdate(todo.id, { completed: !todo.completed });
  };

  return (
    <div className={`todo-item ${todo.completed ? "completed" : ""}`}>
      <div>
        <h2>{todo.title}</h2>
        <h4>{dayjs(todo.createdAt).format("h:mm A, MM/DD/YYYY")}</h4>
        <p>{todo.description}</p>
      </div>
      <div>
        <button onClick={handleToggleCompleted} className={`completed-btn ${todo.completed ? "completed" : ""}`}>
          <FontAwesomeIcon icon={faStar} />
        </button>
        <button onClick={() => setIsEditModalOpen(true)} className="edit-btn">
          <FontAwesomeIcon icon={faPencilAlt} />
        </button>
        <button onClick={() => onDelete(todo.id)} className="delete-btn">
          <FontAwesomeIcon icon={faTrash} />
        </button>
      </div>
      {isEditModalOpen && (
        <Modal
          isOpen={isEditModalOpen}
          onClose={() => setIsEditModalOpen(false)}
          onSave={handleEditSave}
          initialTitle={todo.title}
          initialDescription={todo.description}
        />
      )}
    </div>
  );
};

export default TodoCard;
