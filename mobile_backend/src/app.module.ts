import { Module } from '@nestjs/common';
import { UserModule } from './user/user.module';
import { AuthModule } from './auth/auth.module';
import { PrismaModule } from './prisma/prisma.module';
import { ConfigModule } from '@nestjs/config';
import { PropertyModule } from './property/property.module';
import { AppointmentModule } from './appointment/appointment.module';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true
    }), 
    AuthModule, 
    UserModule,
    PrismaModule,
    PropertyModule,
    AppointmentModule
  ],
})
export class AppModule {}
