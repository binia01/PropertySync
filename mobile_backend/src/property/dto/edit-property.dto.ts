import { IsNumber, IsOptional, IsString } from "class-validator"

export class editPropretyDto{
    @IsString()
    @IsOptional()
    title?: string;

    @IsString()
    @IsOptional()
    description: string;

    @IsNumber()
    @IsOptional()
    price: number;

    @IsString()
    @IsOptional()
    location: string;

}

