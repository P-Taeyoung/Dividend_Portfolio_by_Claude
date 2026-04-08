import { NavLink } from 'react-router-dom';
import styles from './Navbar.module.css';

export default function Navbar() {
  return (
    <nav className={styles.nav}>
      <div className={styles.brand}>월배당 포트폴리오</div>
      <ul className={styles.links}>
        <li><NavLink to="/" className={({ isActive }) => isActive ? styles.active : ''} end>대시보드</NavLink></li>
        <li><NavLink to="/portfolio" className={({ isActive }) => isActive ? styles.active : ''}>포트폴리오</NavLink></li>
        <li><NavLink to="/history" className={({ isActive }) => isActive ? styles.active : ''}>배당 기록</NavLink></li>
      </ul>
    </nav>
  );
}
